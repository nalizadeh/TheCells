::REM--- make TheCells.jar
cd ../bin
C:\PROGRA~2\Java\jdk1.7.0_21\bin\jar cvfm ../build/TheCells.jar ../build/TheCells-Manifest.txt *
cd ../build

::REM--- make Install.jar
cd ../bin
C:\PROGRA~2\Java\jdk1.7.0_21\bin\jar cvfm ../build/Install.jar ../build/Install-Manifest.txt org/nalizadeh/designer/install/*
cd ../build

::REM--- make TheCells.zip

xcopy ..\src\org\nalizadeh\designer\examples org\nalizadeh\designer\examples /e /I
C:\PROGRA~2\Java\jdk1.7.0_21\bin\jar cf TheCells.zip TheCells.jar RunTheCells.cmd org/nalizadeh/designer/examples


::REM--- deploy all InstallTheCells.zip
C:\PROGRA~2\Java\jdk1.7.0_21\bin\jar cf InstallTheCells.zip Install.jar RunInstall.cmd TheCells.zip

del TheCells.jar
del Install.jar
del TheCells.zip
