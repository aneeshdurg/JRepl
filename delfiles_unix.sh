sudo rm -f Test.java
sudo rm -f functions.java
sudo rm -f classes.java

cp Jrepl.class Jrepl
cp InputTxt.class InputTxt
del *.class
mv Jrepl Jrepl.class
mv InputTxt InputTxt.class

cp about.txt about
cp compileerrs.txt compileerrs
cp InputTxt.txt InputTxt
cp help.txt help
cp results.txt results
del *.txt
mv about about.txt
mv compileerrs compileerrs.txt
mv InputTxt InputTxt.txt
mv help help.txt
mv results results.txt