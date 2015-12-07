#!/bin/bash
#!/bin/bash
if [ "$#" -lt 1 ]; then
    echo "Error: Wrong number of arguments!"
    echo "Arguments: a list of camera indexes to connect to. Example: connect_cameras_tmux {1..8}"
    exit 1
fi

function connect {
    local cmds=( $* )
    tmux new-window "ssh rt@argus-${cmds[0]}.student.lth.se"
    unset cmds[0];
    for i in "${cmds[@]}"; do
        tmux split-window -h  "ssh rt@argus-$i.student.lth.se"
        tmux select-layout tiled > /dev/null
    done
    tmux select-pane -t 0
    tmux set-window-option synchronize-panes on > /dev/null
}

connect $*
