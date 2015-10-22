del /F Test.java
del /F Test.class
del /F functions.java
del /F classes.java

copy Jrepl.class Jrepl
copy InputTxt.class InputTxt
del *.class
ren Jrepl Jrepl.class
ren InputTxt InputTxt.class

copy about.txt about
copy compileerrs.txt compileerrs
copy InputTxt.txt InputTxt
copy help.txt help
copy results.txt results
del *.txt
ren about about.txt
ren compileerrs compileerrs.txt
ren InputTxt InputTxt.txt
ren help help.txt
ren results results.txt