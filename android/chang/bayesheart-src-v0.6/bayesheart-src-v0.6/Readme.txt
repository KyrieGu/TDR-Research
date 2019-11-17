-------------------------------------------------------------------
The BayesHeart source code has been released under a BSD license. 
Mobile Interfaces and Pedagogical Systems (MIPS) Group
University of Pittsburgh, Copyright (c) 2015 
http://mips.lrdc.pitt.edu
--------------------------------------------------------------

1. Please refer to the Readme.txt file in the data package for 
  details of the data release. 


2. Source code release:


.  This is release v0.6. There will be releases for new 
features, 
   bug fixes in the future. We also plan to release
 more modules 
   relevant to mobile
 applications in the future. 


.  This package includes source code implementations in java 
   and matlab of the algorithms, as well as the data collection 
   module and demo/debugging module. The sub-folder 
   "Alg_Matlab" includes implementations of ICA (based on the 
   FastICA package: http://research.ics.aalto.fi/ica/fastica/), 
   PCA, HMM in BayesHeart, and FFT. The corresponding jar files 
   for deployment are also included (they have already been 
   imported to the java project). The sub-folder "Alg_Java"
   includes the java implementations of noise reduction techniques,
   as well as pulse counting algorithms including BayesHeart,
   LivePulse, and FFT. The sub-folder "Data Collection" includes the
   data collection app that runs on mobile devices. The sub-folder 
   "Demo_Debugging" includes the demo and debugging module that runs on
   both mobile devices and PCs. Please refer to Section 3 for instructions
   on how to run the code. 

3. Instructions on running the programs:

-3.1 Prerequisites 

.  Verify the MATLAB Compiler Runtime (MCR) is installed and 
   ensure you have installed version 7.16. Download MCR at:
   http://www.mathworks.com/products/compiler/mcr/

.  Verify the Java Runtime Environment (JRE) is installed 
   (version 1.6.0).Download at: http://www.oracle.com/
   technetwork/java/javase/downloads/
   java-archive-downloads-javase6-419409.html 

.  Add system environment variable "JAVA_HOME" and set it 
   to be the folder path of Java jdk (e.g., C:\Program Files\
   Java\jdk1.6.0_35). 

.  Add system environment variable "MCRROOT" and set it 
   to be the folder path of matlab mcr (e.g., 
   C:\Program Files\MATLAB\MATLAB Compiler Runtime)

.  Add the following to the environment variable "PATH": 
   "%JAVAHOME%;%JAVAHOME%\jre\bin;%JAVAHOME%\bin;
    %MCRROOT%\vv716\runtime\win64"

-3.2 To run data collection app on mobile phones

.  Import "BayesHeart_DataCollection" as an existing Android project
   into Eclipse (ADT) workspace.

.  Run the project as "Android Application" on a mobile phone.

.  After the app is launched, click "Start" to start data collection 
  (collects the finger transparency data from the back camera lens).

.  Click "Save Data" to stop the data collection and save the data. 
   The "R/G/B/Y/U/V" values with corresponding time stamps will be saved
   in the corresponding files under "Main Storage/Android/data/edu.pitt.cs.
   mips.BayesHeart_DataCollection" folder of the device storage.


-3.3 To run noise reduction algorithms (i.e., PCA,ICA)

.  Import "Java/BayesHeart" as an existing project into Eclipse 
   workspace. 

.  Open "NoiseReduction.java" located in "Java/BayesHeart/src/"
   
   and update the value of "path" (refer to the commented 
instructions in the file). 

.  Run "NoiseReduction.java" which includes pre-processing (i.e.
   interpolation, intermittent signal handling) and noise 
   reduction methods (i.e. PCA,ICA).

-3.4 To run pulse counting algorithms (i.e., BayesHeart/LivePulse/FFT)

.  Make sure to run "NoiseReduction.java" before this step (refer to 
   Section 3.4).

.  The implementations of BayesHeart (i.e. BayesHeartAlg.java), 
   LivePulse (i.e. LivePulse.java), FFT (i.e. FFT_Alg.java) are included. 
   Choose an algorithm from these three to run. Open the corresponding
   java file (e.g., BayesHeartAlg.java), update "sourceFile" to choose a 
   noise reduction method (refer to the commented instructions in the file),
   as well as the "path" value.

.  Find the heart rate estimations along with the corresponding timestamps
   in the corresponding data folder (e.g., if running BayesHeart on Y
   channel, the results will be in "Y_BayesHeart_hr.csv").

-3.5 To run the demo/debugging module

.  Import "Demo_Debug_Mobile" as an existing Android project into Eclipse
   (ADT) workspace, and launch the application on a mobile phone.

.  Import "Demo_Debug_PC" as an existing Java project into Eclipse workspace,
   and run "ReadLogCat.java"

.  Use the USB cable to connect the mobile phone with the PC, and run these
   two modules together. Then the mobile app will automatically connect to the 
   PC application and the finger transparency signal collected by the mobile phone 
   will be visualized through the PC application in real-time.

----If you have any questions, please email Xiangmin Fan at: xiangmin@cs.pitt.edu 

    Thank you!


--------------------------------------------------------------------
THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
------------------------------------------------------------------