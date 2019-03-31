package customizedparams;

import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTResult;
import org.quifft.output.FrequencyBin;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class UsingZeroPadding {

    private File sineWave600Hz;

    public static void main(String[] args) {
        UsingZeroPadding zeroPaddedWindowsFFT = new UsingZeroPadding();
        zeroPaddedWindowsFFT.computeFFTWithCustomizedParameters();
    }

    private void computeFFTWithCustomizedParameters() {
        // compute an FFT with customized settings
        FFTResult fft = null;
        try {
            // create QuiFFT instance with window sizes of 1000 and num points of 1024
            // (each window will be padded by 1024 - 1000 = 24 zeroes)
            QuiFFT quiFFT = new QuiFFT(sineWave600Hz).windowSize(1000).numPoints(1024);
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
        System.out.println("The first bin, located at " + Math.round(firstBin.frequency) + " Hz, has an " +
                "amplitude of " + Math.round(firstBin.amplitude) + " dB.");
        FrequencyBin mostPowerfulBin = fftFrames[0].bins[14]; // bin closest to 600 Hz
        System.out.println("The 14th bin, located at " + Math.round(mostPowerfulBin.frequency) + " Hz, has an " +
                "amplitude of " + Math.round(mostPowerfulBin.amplitude) + " dB.");
    }

    private UsingZeroPadding() {
        ClassLoader classLoader = getClass().getClassLoader();
        sineWave600Hz = new File(classLoader.getResource("600hz-sine-wave.wav").getFile());
    }

}
