package spectrumvisualization;

import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTStream;
import org.quifft.output.FrequencyBin;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Program to visualize the frequency spectrum of a song over time.
 * When the program runs, it plays the song while a frequency spectrum graph animates in sync.
 * The values for the frequency spectrum graph are obtained through a Fast Fourier Transform (FFT).
 * This visualizer uses the QuiFFT library to compute the FFT result for the song.
 *
 * To try it with your own .wav, .aiff, or .mp3 files, add them to the /resources folder and
 * update the class's private constructor.
 *
 * Credits for the royalty-free example song are below.
 * Artist: JPB
 * Title: High [NCS Release]
 * Youtube: https://youtu.be/Tv6WImqSuxA
 * Music provided by NoCopyrightSounds
 * Music promoted by Audio Library: https://youtu.be/R8ZRCXy5vhA
 */
public class SpectrumVisualizer {

    // Song to play
    private File song;

    // FFTStream used to compute FFT frames
    private static FFTStream fftStream;

    // Next frame to graph
    private static FFTFrame nextFrame;

    // Wrapper for JFreeChart line graph
    private static FFTGrapher grapher = new FFTGrapher();

    /**
     * Initialize audio file here
     */
    private SpectrumVisualizer() {
        ClassLoader classLoader = getClass().getClassLoader();
        song = new File(classLoader.getResource("High.mp3").getFile());
    }

    public static void main(String[] args) {
        SpectrumVisualizer visualizer = new SpectrumVisualizer();
        grapher.initializeGraph();
        visualizer.visualizeSpectrum();
    }

    private void visualizeSpectrum() {
        // Obtain FFTStream for song from QuiFFT
        QuiFFT quiFFT = null;
        try {
            quiFFT = new QuiFFT(song).windowSize(8192).windowOverlap(0.75);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        fftStream = quiFFT.fftStream();
        System.out.println(fftStream);

        // Compute first frame
        nextFrame = fftStream.next();

        // Start playing audio
        try {
            InputStream in = new FileInputStream(song);
            AudioStream audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate time between consecutive FFT frames
        double msBetweenFFTs = fftStream.windowDurationMs * (1 - fftStream.fftParameters.windowOverlap);
        long nanoTimeBetweenFFTs = Math.round(msBetweenFFTs * Math.pow(10, 6));

        // Begin visualization cycle
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(SpectrumVisualizer::graphThenComputeNextFrame, 0, nanoTimeBetweenFFTs, TimeUnit.NANOSECONDS);
    }

    private static void graphThenComputeNextFrame() {
        // Graph currently stored frame
        FrequencyBin[] bins = nextFrame.bins;
        long timestamp = (long) nextFrame.frameStartMs / 1000;
        grapher.updateFFTData(bins, timestamp);

        // If next frame exists, compute it
        if(fftStream.hasNext()) {
            nextFrame = fftStream.next();
        } else { // otherwise song has ended, so end program
            System.exit(0);
        }
    }
}