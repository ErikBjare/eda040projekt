#!/bin/bash
function distribute(){
    local cams=( $* )
    tmux new-window "scp ./build_bin/build/RealCameraMain rt@argus-${cams[0]}.student.lth.se:~/SACS"
    unset cams[0];
    for i in "${cams[@]}"; do
        tmux split-window -h  "scp ./build_bin/build/RealCameraMain rt@argus-$i.student.lth.se:~/SACS"
        tmux select-layout tiled > /dev/null
    done
    tmux select-pane -t 0
    tmux set-window-option synchronize-panes on > /dev/null
}
distribute $*
