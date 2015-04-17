# SmartTuner
An Android application to detect patterns in a musician's intonation on his or her instrument

### Notes
This project is intended to be the central repository where I'll aggregate code from past projects that implemented only select components of the final application.

### Application Structure
* Main Activity
  * Tuner module
    * tuner loop: (Tuner thread)
      * fill buffer with PCM data from mic
      * get input signal frequency via autocorrelation
      * convert frequency to pitch and store in `currentPitch`
    * `currentPitch` variable can be read from other modules
  * Interpreter module
    * interpreter loop: (Interpreter thread)
      * insert `currentPitch` from Tuner into data set
        * think more on this
    * when `getAnalysis` called, recognize patterns in pitches and return statistics
  * Display component
    * get Analysis from interpreter and display somehow
