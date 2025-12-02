VARIANT=desktop-xfce
WORKSPACE_PORT=NEXT

ARGS+=(
    --keep-alive
    --silence-build
)

# Set desktop resolution to 1920x1080
RUN_ARGS+=(
    -e GEOMETRY=1920x1080
)