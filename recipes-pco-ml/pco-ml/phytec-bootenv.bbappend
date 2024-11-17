FILESEXTRAPATHS:prepend := "${THISDIR}/pco-ml:"

SRC_URI = " \
	file://bootenv_.txt \
"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

do_deploy:append() {
	rm -f ${DEPLOYDIR}/bootenv.txt
	install -m 0644 ${S}/bootenv_.txt ${DEPLOYDIR}/bootenv.txt
}
addtask deploy before do_build after do_unpack
