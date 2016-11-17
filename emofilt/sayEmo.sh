#!/bin/bash
	echo $2 | java -jar emofilt.jar -mary -voc $3 -e $1 -of tmp_e.pho;
	PhoPlayer.exe database="D:\Programme\mbrola\\"$3"\\"$3 tmp_e.pho /o=test.wav /t=wav;
	mv TEST.WAV $4;
	rm tmp_e.pho;
# ./sayEmo.sh "joy" "das ist mein persönlicher text" "de6" "blablubb.wav"