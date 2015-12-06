rm -rf build_src
mkdir -p build_src
cp -r src/common src/server_util src/server/ src/RealCameraMain.java build_src
cp -r crosscompilation/* build_src

./build_axism3006v.sh

scp ./build_bin/build/RealCameraMain rt@argus-1.student.lth.se:~/vemartony.exe
