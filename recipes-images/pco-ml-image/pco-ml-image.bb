require recipes-images/images/phytec-headless-image.bb

# based from layer:
# https://git.phytec.de/meta-qt6-phytec/tree/recipes-images/images/phytec-qt6demo-image.bb?h=kirkstone

SUMMARY = "PCO image for testing Vision4CE machine learning."

IMAGE_FEATURES += "\
    splash \
    ssh-server-openssh \
    hwcodecs \
    qtcreator-debug \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston', '', d)} \
"

LICENSE = "MIT"

IMAGE_INSTALL += "\
    packagegroup-base \
    packagegroup-gstreamer \
    ${@bb.utils.contains("DISTRO_FEATURES", "virtualization", "packagegroup-virtualization", "", d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland qtwayland-plugins weston weston-init', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'weston-xwayland', '', d)} \
    qtphy \
    qtphy-images \
"
#   qtphy-service

IMAGE_INSTALL:append:mx8mp-nxp-bsp = "\
	htop iotop apt zip xz mc nano lsof tcpdump netcat socat screen tree minicom openvpn \
	openssl libmcrypt \
	python3 python3-pip python3-requests python3-fcntl python3-pygobject \
	packagegroup-camera \
	isp-imx-phycam \
	opencv opencv-samples opencv-apps python3-opencv \
	gstreamer1.0 gstreamer1.0-plugins-good gstreamer1.0-plugins-bad-opencv \
	python-phycam-margin-analysis \
	packagegroup-imx-ml \
	pco-ml \
"

IMAGE_INSTALL:append = " gstreamer1.0-rtsp-server glfw boost freetype liberation-fonts"
PACKAGECONFIG:append:pn-glfw = " wayland"
