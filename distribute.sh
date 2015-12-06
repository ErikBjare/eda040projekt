for i in 1 2 3 4 5 6 7 8; do
    scp ./build_bin/build/RealCameraMain "rt@argus-$i.student.lth.se:~/SACS"
done
