# Code Examples for QuiFFT

This repository contains several examples of [the QuiFFT Fourier transform library](https://www.github.com/mileshenrichs/QuiFFT) being used to compute FFTs on digital audio files.  

These examples should be useful to look at if you're considering using QuiFFT and would like to know what its API and output objects look like.  A good way to learn how QuiFFT works would be to clone the examples, run them, and look at the print statements and which output objects and fields they get their information from.

These examples include:

- [__Your First FFT__](): the simplest code possible to compute an FFT using QuiFFT's default parameters (Hanning windows of size 4096 and 50% overlap, no zero-padding, amplitudes in decibels)
- [__Rectangular Window__](): an FFT that uses rectangular windows instead of Hanning windows, and modifies window size and overlap to 1024 and 75%, respectively
- [__Normalized Linear Scale__](): an FFT whose frequency bin amplitude outputs are scaled linearly (not in dB, which in contrast is a logarithmic scale) and normalized such that each amplitude is between 0 and 1
- [__Using Zero Padding__](): combining `.windowSize()` and `numPoints()` configuration methods to add zero-padding to sampling windows before their FFTs are computed, which allows us to choose a sampling window of 1000, which isn't a power of 2 (without zero-padding this would create a `BadParametersException`)

---

To see QuiFFT's full documentation, refer to [its README](https://github.com/mileshenrichs/QuiFFT/blob/master/README.md).