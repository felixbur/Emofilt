# make lanugage awk
# helper script to construct new languages for emofilt languages.xml syntax
# just write you phonems after the class, like this
# long_vowel
# short_vowel
# approximant
# nasal
# fricative_voiced
# fricative_voiceless
# stop_voiced
# stop_voiceless
#
# e.g.: stop_voiceless p t k
BEGIN{}
{
i=0;
 if ($1=="long_vowel"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"true\"/>";
   }
 } else if ($1=="short_vowel"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"true\"/>";
   }
 } else if ($1=="approximant"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"true\"/>";
   }
 } else if ($1=="nasal"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"true\"/>";
   }
 } else if ($1=="fricative_voiced"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"true\"/>";
   }
 }  else if ($1=="fricative_voiceless"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"false\"/>";
   }
 } else if ($1=="stop_voiced"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"true\"/>";
   }
 }  else if ($1=="stop_voiceless"){
   for (i=2;i<=NF;i++) {
     print "<phoneme name=\""$i"\" manner=\""$1"\" voiced=\"false\"/>";
   }
 }
}
END{
	print"<!-- silence -->";
	print"<phoneme name=\"_\" manner=\"silence\" voiced=\"false\"/>";
}
