Hello
This is an emofilt distribution. You should get further information at
http://emofilt.sourceforge.net/
The newest version of emofilt should always be  available there via the
git-repository.


DESCRIPTION

emoFilt is NOT a text-to-speech synthesizer, it's ONLY a filter between
an MBROLA pho-file and the MBROLA-engine to "emotionalize" the speech.
You NEED at least MBROLA in you path to use emofilt.

DISTRIBUTION

if you unpacked the distribution the following files should be there
Readme.txt  - this file
emofilt_software_user_agreement.txt - license agreement
emofilt.jar - program as jar-file

configuration files:
emofiltConfig.ini - main configuratio file, this needs DEFINITELY to be adapted before first use!
languages.xml  - voice-specification file
emotions.xml - example emotion-specification file
logConfig.xml - log4j-configuration
taggergui.config - configuration file for the storytelling interface

examples:
sayEmo.sh - example script to run emofilt with Mary TTS
emofilt.bat/emofilt.sh - example batch file to start Gui with button-click
runTaggerGUI.sh/runTaggerGUI.bat - example files to start the storytelling interface

scripts:
makeLanguage.awk - script to generate new languages.xml entries

directory test - several PHO-files for testing
directory docs - web documentation
directory src - source-files
directory init - initilization files for the modification plugins, only needed for building



INSTALL

Emofilt needs Java 1.5 to be installed. Other versions might
work, but I didn't try them.
Unpack the zip-archive. All pathes are stored in the
emofiltConfig.ini files, open it (or use one of the templates provided
for your operating system) with a text-editor.
The most important path you will have to configure (besides the paths
to Mbrola-programs) is the workingDir (right on top of the file).
Specify here the directory where the other configuration files
(languages.xml, emotions.xml and loggerConfig.xml) are stored.
Then either (if windows) click emoFilt.bat or type
java -jar emofilt_fat.jar -useGui on a commandline in this emofilt-
folder.

You can run emofilt from anywhere if you specify the path to its
configuration file with the -cf option.
Some fixed values should be specified in this configuration file, but
you can specify a different one with the -cf argument).
In order to get acoustic output ("play-button") you must configure
the paths in the configuration file and have MBROLA installed.
The default configuration file is in English, but you can write
your own for your language by translating the wordings.
In order to have the "new utterance" command in the Gui-interface work,
you have to have installed and configured (in the configuration-file) a
text-to-phofile  converter program, e.g. MARY TTS.

USAGE

Because emofilt is a java program you need Java to run it.

There are three interfaces:
- 1.  Emofilt Developer is a graphical interface (Gui) that lets you
  generate emotion-descriptions.
  You can call it with the option -useGui, so you would type on the
  commandline (from inside the program directory):
  java -jar emofilt_fat.jar -useGui
  and you should see a window popping up with the interface. First
  thing you might do then is loading an utterance via the
  utterance.load menu-items.
  Help is provided mainly by tooltips, which should pop up if you
  linger with the pointer over the panels.

  2. The story tagger interface displays an editor that can be marked up with
  emotions. You can call it from the command-line by providing the path to its
  config-file, e.g.:
  java  -cp emofilt/emofilt.jar; emofilt.storytagger.TaggerGUI taggergui.config

- 3. the same program can be used as a commandline interface, if you want
  to use it as a filter between a text-to-phoneme converter program
  and Mbrola.
  here is the output if the program's usage message:
  $ java -jar  emofilt_fat.jar -h
  emofilt version 0.94
  Usage: program [-h ] [-version ] [-useGui ] [-mary ] [-if ] [-of ] [-e ] [-voc ] [-cf ] [-ef ] [-lf ] [-lcf ] [-lv ]

        -h 'print usage'  Default: false
        -version 'print version'  Default: false
        -useGui 'display the graphical developer interface'  Default: false
        -mary 'send input to mary server for text-to-speech'  Default: false
        -if 'specify input pho-file'  Default: System.in
        -of 'specify output pho-file'  Default: System.out
        -e 'specify emotion (must appear in emotions-description)'  Default: untitled
        -voc 'specify voice'  Default:
        -cf 'specify configuration-file'  Default: emofiltConfig.ini
        -ef 'specify emotion-description xml-file'  Default: emotions.xml
        -lf 'specify voice-description xml-file'  Default: languages.xml
        -lcf 'specify logger configuration log4j-file'  Default: logConfig.xml
        -lv 'specify log-level [debug warn error fatal off]'  Default: debug
        -gr 'global rate: damp or amplify the modifications for graded emotions between -1 and 1'  Default: 0

  many of the required input files can be specified in the emofilt
   config-file.



EXAMPLE

Follows an example-script that utters a phrase emotionally, if you use
the cygwin bash under windows and txt2pho
$ cat sayEmotional.bat
echo $2 > tmp.txt;
echo "" > txt2pho.ini;
txt2pho.exe tmp.txt tmp.pho;
java -jar "D:\Work\emoFilt\emofilt\emofilt_fat.jar" -lv off -cf
"D:\Work\emoFilt\emofilt\emofiltConfig.ini" -e $1 -if tmp.pho
-of tmp_e.pho;
PhoPlayer.exe database="D:\Programme\Mbrola\de6\de6" tmp_e.pho;

or, if the Mary server is running:
$ cat sayMary.sh
echo $2 | java -jar emofilt_fat.jar -mary -voc us1 -e $1 -of tmp_e.pho;
PhoPlayer.exe database="D:\Programme\Mbrola\us1\us1" tmp_e.pho;



the same function as a bash-script under linux
$ cat sayEmotional.sh
echo $2 | txt2pho | java -jar /home/felixbur/emofilt/emofilt.jar -cf /home/felixbur/emofilt/emofiltConfig.ini -e $1 | mbrola /home/felixbur/de1/de1 - -.au | play -t au -;
If you have the Mary text-to-speech server running, you could also omit
txt2pho and pass orthography directly to emofilt like this:
echo $2 | java -jar /home/felixbur/emofilt/emofilt.jar -mary -cf /home/felixbur/emofilt/emofiltConfig.ini -e $1 | mbrola /home/felixbur/de1/de1 - -.au | play -t au -;


If I type now
$ sayEmotional.sh happiness "am Wochenende scheint mal wieder die Sonne."
I hear the sentence played in a happy mood ;-)



enjoy!

PS:
If you should write new language, emotion or config files, e.g.
for new voices/languages,
I'll be happy to include them in future distributions.

CHANGELOG
1.1 
- fixed bug that allowed pitch to go below zero because pause length was used in descending pitch calcule.
1.0
- allowing zero duration for silence phoneme
0.99
- fixed bug that caused current directory to appear before path values
- lines in tagger gui can now be selected by double click
- fixed bug in storytagger that prevented proper synthesis
- added tabbed pane to story tagger to show resulting  xml code
0.98 24/02/11
- added first version of story tagger GUI
- added some library interfaces to call pho-file creation from outside the package
- replaced Mary phonetization server interface version 3.6 with version 4.3
- added us3
0.97 26/08/10
- added menu entries to create new emotion names and remove them.
0.96 12/07/10
- handle audio playback now with internal java audio library
0.95 04/02/08
- changed replacements of language descriptors for de[1,2,3,4,5] from 6-a: to a-a:.
- enhanced readability of messages in GUI
- changeLastSylContour now changes last syllable containing a voiced phoneme.
- fixed bug in modelContour causing zero f0-values at utterance end.
- added Dutch: nl2
0.94 28/01/08
- added global rate for graded emotion simulation: possibility to dampen or amplify all modifications by a single factor.
0.93 10/10/07
- make GUI geometry configurable
- interpolate missing pitch contour description for voiced phonemes.
- read and writes now also pitch information for unvoiced phonemes
- added save PHO file functionality
- removed the automatic modeling of f0-contours when reading in a PHO file
- fixed some serious bugs concerning the modeling of f0-contours (still needed for jitter and wave-model)
0.91 26/9/07
- fixed bug: couldn't read in files starting with vowel
- changed pitch interpolation to interpolate between phoneme borders
0.9 23/5/06
- migrated code to java 1.5.0
- added interface to Mary (http://mary.dfki.de/) Text-to-speech in the
GUI. Start the Mary server and edit the PhoGenCmd section in the
config.ini if you want to use it. You can call the Mary-server via the
GUI (new utterance) or via the commandline (-mary option).
- fixed bug: wave-model plugin caused zero-pitch values if utterance starts or
ends with voiceless syllables.
- fixed bug: globally descending f0-contour had no syllable-focus.
- fixed bug: plugin lastSylContour didn't work on utterances with unvoiced last syllables.
0.8 10/9/05
- fixed bug: modifications were done repeatedly
-  new feature: can specify Colors by names (as specified at
http://www.w3schools.com/html/html_colornames.asp)
- fixed bug: rewritten Syllabelizer, many bugs fixed
0.7 29/8/05
- added plugin architecture for modifications (see extra docs on how
to write own modifications)
- changed almost all modification-names : old emotions.xml files won't
work any more
- fixed bug: change last sylcontour didn't work for English
0.6 28/2/05
- changed -v option to -version
- fixed bug: -voc option wasn't settable
- tested with Linux (Suse 9.0)
- changed pathes to external commands in the config-file
- renamed config-file extension from .dat to .ini
0.5 22/2/05
- changed rate-settings for jitter and pauses
- configuration: removed paths to config-files and added workingDir
- fixed bug: help-file wasn't readable from jar-file
- added save-utterance button
- showHelp didn't work with jar-file.
- fixed bug: overlong error messages caused Gui-widgets to collaps
- added menu icons
- fixed bug: close window in GUI didn't exit program
- added looks jgoodies look and feel
- fixed bug: no tooltip  for changeLastSylContour
0.4 15/2/05
- fixed bug: if reading emotion-descriptions from non-English denotations
   pitch-contours, vowel-targets and voice-effort were ignored
- look and feel can now be set in config-file
- fixed bug: starting a pho-file with pauses gave nullpointer in syllablelize
- added defaultVoice to config-file
- fixed bug: duration and time f0-values couldn't be specified as Doubles
- Gui starts now also without initial utterance
0.3 27/1/05
- added German configuration file for distribution
- added gender option for NLP-processor in the GUI.
- Added tooltips.
- Added description-element for emotions (shows as tooltip).
- fixed bug: No error message appeared if NLP-program wasn't configured.
- fixed bug: Foreground color in modification-label wasn't setable
0.2 12/1/05
- added command-line interface.
0.1 10/1/05
- very first alpha version, not yet tested.


