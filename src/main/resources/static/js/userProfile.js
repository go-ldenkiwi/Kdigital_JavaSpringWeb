function closeFollow() {
	document.querySelector(".modal-follow").style.display = "none";
}
document.querySelector(".modal-follow").addEventListener("click", (e) => {
	if (e.target.tagName !== "BUTTON") {
		document.querySelector(".modal-follow").style.display = "none";
	}
});

document.querySelector("#subscribeBtn").onclick = (e) => {
  e.preventDefault();
  document.querySelector(".modal-follow").style.display = "flex";
};

