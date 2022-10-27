let imgContainer=document.getElementById("imgContainer");

function setThumbnail(event) {
	if(imgContainer.firstChild){
		imgContainer.innerHTML="";
	}
    for (let image of event.target.files) {
      let reader = new FileReader();

      reader.onload = function(event) {
        let img = document.createElement("img");
        img.setAttribute("src", event.target.result);
        document.querySelector("div#imgContainer").appendChild(img);
      };

      reader.readAsDataURL(image);
    }
}
