del %1.txt
echo off
cls
set /p input="Press enter to input txt: "
echo %input% > %1.txt
exit
