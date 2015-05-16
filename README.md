# SmartTuner
An Android application to detect patterns in a musician's intonation on his or her instrument


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


### Notes
This project is intended to be the central repository where I'll aggregate code from past projects that implemented only select components of the final application.

Implementation
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
      * could have a currentAnalysis field analagous to Tuner's current pitch--better for realtime updates to UI
  * Display component
    * get Analysis from interpreter and display somehow
      * histogram-like display per note

Find a PDA library!!!

### Features to Implement
* overall display (display full analysis) along with instantaneous display (currentPitch)
* pitch ranges where close enough to perfectly in tune to be considered 'good'. Else 'bad'
* beginner-expert slider in settings to control size of 'good' and 'bad' pitch ranges
* first-time tutorial overlay