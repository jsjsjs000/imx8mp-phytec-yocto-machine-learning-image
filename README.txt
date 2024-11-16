#		Copy or download meta layer to Phytec Yocto 23.1.0 for i.MX 8M plus
cd ~/phyLinux/sources/
# mkdir -p ~/phyLinux/sources/meta-pco-ml/
git clone https://github.com/jsjsjs000/imx8mp-phytec-yocto-machine-learning-image

#		Initialize Phytec Yocto 23.1.0 environment
cd ~/phyLinux
source sources/poky/oe-init-build-env


	Write  to SD card
sync; umount /media/$USER/boot; umount /media/$USER/root

lsblk -e7
#> mmcblk0               179:0    0  14,8G  0 disk  
#> ├─mmcblk0p1           179:1    0    65M  0 part  
#> └─mmcblk0p2           179:2    0   1,6G  0 part  

sudo pv -tpreb deploy/images/phyboard-pollux-imx8mp-3/pco-ml-image-phyboard-pollux-imx8mp-3.wic | sudo dd of=/dev/mmcblk0 bs=1M oflag=sync; sync


overlays=imx8mp-isi-csi1.dtbo imx8mp-vm017-csi1.dtbo imx8mp-isi-csi2.dtbo imx8mp-vm017-csi2.dtbo

setup-pipeline-csi1 -f SGRBG8_1X8 -s 1920x1080 -o '(336,432)' -c 1920x1080


gst-launch-1.0 v4l2src device=/dev/video0 ! video/x-bayer,format=grbg,depth=8,width=2592,height=1944 ! bayer2rgbneon ! fpsdisplaysink sync=false

gst-launch-1.0 v4l2src device=/dev/video0 ! video/x-raw,width=1920,height=1080 ! fpsdisplaysink sync=false

# composite 2 streams: camera 30fps + testvideo 30fps, result 30fps - CPU 1 core 100%
gst-launch-1.0 imxcompositor_g2d name=comp sink_1::xpos=0 sink_1::ypos=0 sink_1::alpha=0.5 ! \
  waylandsink window-width=1920 window-height=1080 \
  v4l2src device=/dev/video0 ! video/x-raw,width=1920,height=1080,framerate=30/1,pixel-aspect-ratio=1/1 ! comp.sink_0 \
  videotestsrc ! video/x-raw,width=1920,height=1080,framerate=30/1,pixel-aspect-ratio=1/1 ! comp.sink_1

----------------------------------------------------------------------------------------------------




	Add files to boot partition
cp ~/Desktop/jarsulk-pco/programs/imx8mp/m7/imx8mp_m7_led_demo/armgcc/debug/imx8mp_uart_server.bin ../sources/meta-phytec/recipes-bsp/bootenv/phytec-bootenv
	# VM version:
# copy file to vm:
cp ~/Desktop/jarsulk-pco/programs/imx8mp/m7/imx8mp_m7_led_demo/armgcc/debug/imx8mp_uart_server.bin vm_share/
# copy file from vm to phyLinux:
cp /mnt/vm/imx8mp_uart_server.bin ../sources/meta-phytec/recipes-bsp/bootenv/phytec-bootenv

code ../sources/meta-phytec/recipes-bsp/bootenv/phytec-bootenv.bb
# ----------------------------------------
SRC_URI = " \
	file://imx8mp_uart_server.bin \

do_deploy() {
	install -m 0644 ${S}/imx8mp_uart_server.bin ${DEPLOYDIR}
# ----------------------------------------

code ../sources/meta-phytec/conf/machine/include/phyimx8.inc
# ----------------------------------------
IMAGE_BOOT_FILES += "imx8mp_uart_server.bin"
# ----------------------------------------



	Output files
xdg-open ~/phyLinux/build/tmp/work/phyboard_pollux_imx8mp_3-phytec-linux/phytec-qt6demo-image/1.0-r0/rootfs/
