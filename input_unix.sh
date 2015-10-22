del %1.txt
echo off
clear
echo "Press enter to input text: "
read input
echo %input% > %1.txt
exit
