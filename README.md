# InTune
An Android application to detect patterns in a musician's intonation on his or her instrument


### Application Structure
* Main Activity
  * Tuner module
    * tuner loop: (Tuner thread)
      * use TarsosDSP mechanism for getting mic data and running YIN PDA to get frequency
      * filter frequency to remove noise
      * convert filtered frequency to pitch and store in `currentPitch`
    * `currentPitch` variable can be read from other modules
  * Interpreter module
    * interpreter loop: (Interpreter thread)
      * insert `currentPitch` from Tuner into data set
        * think more on this
      * maintain analysis  based on data set
      * 'currentAnalysis' variable can be read from other modules
  * Display component
    * on display loop, get Analysis from interpreter and display somehow


### Build Process

#### Release APK Signing
Keystore information for signing the release APK is specified in gradle.properties using the keys
intuneKeystore, intuneKeystorePassword, intuneKeyAlias, andintuneKeyPassword. Builds in environments
without access to the keystore will not fail but the release APK will be left unsigned.


### Notes
This project is intended to be the central repository where I'll aggregate code from past projects that implemented only select components of the final application.

#### Implementation
* Main Activity
  * Tuner module
    * tuner loop: (Tuner thread)
      * TarsosDSP handles pitch detection
      * filter frequencies (think more here)
        * smoothing filter on non-null readings (this needs work--shouldn't cause false readings when note changes)
        * require several non-null (no pitch detected) readings before affecting the `currentPitch`
      * convert filtered frequency to pitch and store in `currentPitch`
    * `currentPitch` variable can be read from other modules
  * Interpreter module
    * interpreter loop: (Interpreter thread)
      * insert `currentPitch` from Tuner into data set
        * think more on this
    * maintain an analysis of patterns in pitches and return statistics
      * could have a currentAnalysis field analagous to Tuner's current pitch--better for realtime updates to UI
  * Display component
    * get Analysis from interpreter and display somehow
      * histogram-like display per note


### Features to Implement
* overall display (display full analysis) along with instantaneous display (currentPitch)
* pitch ranges where close enough to perfectly in tune to be considered 'good'. Else 'bad'
* beginner-expert slider in settings to control size of 'good' and 'bad' pitch ranges
* first-time tutorial overlay
