# SonificationStudien
Sonifkation of errors.

This program provides an auditive warn system of errors, which occure in the vicon environment of the RaHM-Lab in DHBW Karlsruhe. This document shall help to setup the system provided. 

### Quick Setup:

1. Clone this project

    git clone https://github.com/MobMonRob/SonificationStudien.git

2. Pull submodules

    git pull --recurse-submodules
  
3. Execute the maven install for the vicon datastream sdk

    ./maven-install-JViconDataStream.sh

4. Build project with maven (the SonificationStudien project)

    mvn clean install
     
5. Build project with maven (the JavaAudioSynthesizer project)

    cd JavaAudioSynthesizer/JavaAudioSynthesizer
    mvn clean install
     
 5. Use the programm either through the jar created in the .m2 directory or through your IDE (execute main method in FaultMonitor)
    
### Architecture
The system is build in the following architecture. 

   ![Architektur-Sonifikation](https://user-images.githubusercontent.com/59008578/168747143-74996b97-e3f5-4cd9-a2af-03fdb05a22f5.png)
   
Therefore, if a new way of detecting faults is needed, a new implementation of FaultDetector can be developed and be given to Fault Monitor. The only current implementation now is NotVisibleMarkerDetector, which uses the MarkerTracker class. 

If a new way of producing sound is needed, a new implementation of SoundFactory can be developed and implemented in Fault Monitor class. Current implementations are CountRealted and CoordinateRelated SoundFactories. 

The FaultMonitor is iterating over all fault detectors and asking the FaultDetectors every time if a fault is currently detectable. If, then the fault is compared to the last detected fault. If it's a new fault, which is not currently sonificated, the fault is passed to the current sound factory. 

### Prospects
In prospect are the following proposed changes:
  - Implement a way to comfortably change the mode of sonification / change the used Sound Factory
  - Develop other fault detectors, which for example detect a deactivated camera. 
  - Add a graphical user interface to the program, so it can be used as standalone

### Latest changes 
The following changes were newly implemented, but not fully tested. So maybe the features should be assured again. 

  - Feature 1: You can give a list of marker objects to FaultMonitor, so only the given Markers are observed. If the list is empty, all activated markers are observed
  - Feature 2: Acoustic Signals, which are played to sonificate a fault, are now constantly played, until the fault to sonificate is not longer detectable. 


