#!/bash/sh

ERROR_FIND_LOG="/home/$USERNAME/error_find.log"
ERROR_REMOVE_LOG="/home/$USERNAME/error_remove.log"
REMOVE_LOG="/home/$USERNAME/remove.log"

if [ -z $1 ]; then
    echo 'please input folder name'
    return 1
else
    find "$1" -name "*.tmp" -print0 2> $ERROR_FIND_LOG | xargs -0 rm -v > $REMOVE_LOG 2> $ERROR_REMOVE_LOG
fi