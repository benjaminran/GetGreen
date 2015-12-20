# PitchPatterns
An Android application to detect patterns in a musician's intonation on his or her instrument

# Download
Beta release coming soon


### CI Server
[![Build Status](http://52.35.114.95:8080/buildStatus/icon?job=PitchPatterns)](http://52.35.114.95:8080/job/PitchPatterns/)

Jenkins is active at [http://52.35.114.95:8080](http://52.35.114.95:8080).


### Build Process

#### Release APK Signing
Keystore information for signing the release APK is specified in gradle.properties using the keys
appKeystore, appKeystorePassword, appKeyAlias, and appKeyPassword. Builds in environments
without access to the keystore will not fail but the release APK will be left unsigned.

