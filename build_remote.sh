cp -r src/ build_src

./build_axism3006v.sh

scp ./build_bin/build/Main rt@argus-1.student.lth.se
