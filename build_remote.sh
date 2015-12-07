#!/bin/bash
if [ "$#" -lt 2 ]; then
    echo "Error: Wrong number of arguments!"
    echo "First argument: the ssh target (ex. user@server.internet.com)"
    echo "Second argument: the remote directory where the build should be done. Remember to put the path in quotes if your shell could expand the it before sending. (ex. "~/eda040projekt")."
fi
SSH_TARGET=$1
REMOTE_DIR=$2

# Copy the relevant source to the destination
scp -Cr ./src ./crosscompilation $1:$2
# Execute the build
ssh $SSH_TARGET << ENDSSH
cd $REMOTE_DIR
rm -rf build_src
mkdir -p build_src
cp -r src/common src/server_util src/server/ src/RealCameraMain.java build_src
cp -r crosscompilation/* build_src
./build_axism3006v.sh
ENDSSH

# Copy all error logs and if possible the main exe back here
mkdir -p ./build_bin/build
scp $1:$2/build_bin/{build/build.err,build/build.log,build.log,build.err} ./build_bin/build/
scp $1:$2/build_bin/build/RealCameraMain ./build_bin/build/RealCameraMain

# After this, use ./distribute.sh to mass connect to all the cameras
