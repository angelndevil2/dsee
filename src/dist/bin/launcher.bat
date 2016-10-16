set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set DSEE_HOME=%DIRNAME%..

%DIRNAME%dsee.bat -d %DIRNAME%.. $*