package customizedparams;

import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTResult;
import org.quifft.output.FrequencyBin;
import org.quifft.params.WindowFunction;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SmallRectangularWindow {

    private File sineWave600Hz;

    public static void main(String[] args) {
        SmallRectangularWindow rectangularFFT = new SmallRectangularWindow();
        rectangularFFT.computeFFTWithCustomizedParameters();
    }

    private void computeFFTWithCustomizedParameters() {
        // compute an FFT with customized settings
        FFTResult fft = null;
        try {
            // create QuiFFT instance with window size of 1024 samples and overlap of 75%
            QuiFFT quiFFT = new QuiFFT(sineWave600Hz)
                    .windowFunction(WindowFunction.RECTANGULAR).windowSize(1024).windowOverlap(0.75);
            fft = quiFFT.fullFFT();
        } catch (IOException e) {
            System.out.println("An I/O exception occurred while QuiFFT was opening an input stream to the audio file");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("QuiFFT was given an invalid audio file");
        }

        // print the FFTResult to see details about the transformation and the audio file on which it was performed
        System.out.println(fft);

        // get individual frames (sampling windows) from FFT
        FFTFrame[] fftFrames = fft.fftFrames;
        System.out.println("There are " + fftFrames.length + " frames in this FFT, each of which was computed " +
                "from a sampling window that was about " + Math.round(fft.windowDurationMs) + " milliseconds long.");

        // inspect amplitudes of individual frequency bins in the first frame
        FrequencyBin firstBin = fftFrames[0].bins[0];
        System.out.println("The first bin, located at " + Math.round(firstBin.frequency) + " Hz, has an amplitude of " +
                Math.round(firstBin.amplitude) + " dB.");
        FrequencyBin mostPowerfulBin = fftFrames[0].bins[14]; // bin closest to 600 Hz
        System.out.println("The 14th bin, located at " + Math.round(mostPowerfulBin.frequency) + " Hz, has an " +
                "amplitude of " + Math.round(mostPowerfulBin.amplitude) + " dB.");

        // remarks about window size's effect on frequency resolution
        System.out.println();
        System.out.println("Notice that the frequency resolution has worsened from the \"Your First FFT\" example " +
                "because we're taking sample windows of smaller size (1024 samples instead of the default 4096).");
    }

    private SmallRectangularWindow() {
        ClassLoader classLoader = getClass().getClassLoader();
        sineWave600Hz = new File(classLoader.getResource("600hz-sine-wave.wav").getFile());
    }

}
