:: first->delete the file TheCellsKeys, password->namadaro alias->jdc

cd ../bin
C:\PROGRA~2\Java\jdk1.7.0_40\bin\jar cvfm ../build/TheCells.jar ../build/TheCells-Manifest.txt *
cd ../build

C:\PROGRA~2\Java\jdk1.7.0_40\bin\keytool -v -genkey -keystore TheCellsKeys -alias jdc -keyalg RSA -keysize 2048 -validity 10000
C:\PROGRA~2\Java\jdk1.7.0_40\bin\jarsigner -keystore TheCellsKeys TheCells.jar jdc

