// img 미리보기
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


// boardReply
const replyInserForm=document.forms["replyInsertForm"]
const replyList=document.getElementById("replyList")

if(replyInserForm!=null){
	replyInserForm.addEventListener("submit", async(e)=>{
		e.preventDefault();
		let boardNo=replyInserForm.boardNo.value;
		let url=replyInserForm.action;
		let data=new FormData(replyInserForm);
		
		let resp=await fetch(url, {body:data, method:"post"});
		if(resp.status==200){
			let json=await resp.json();
			let msg="";
			switch(json.status){
				case 0: msg="등록 실패"; break;
				case 1: 
					msg="등록 성공"; 
					readReplyList(boardNo);
					break;
				case -1: msg="로그인 하세요"; break;
			}
			alert(msg);
		}
	});
}

async function readReplyList(boardNo){
	let url="/reply/list.do?boardNo="+boardNo;
	let resp=await fetch(url);
	if(resp.status==200){
		let textHtml=await resp.text();
		replyList.innerHTML=textHtml;
	} else {
		alert("댓글 리스트 불러오기 실패");
	}
}

async function readReplyUpdateForm(replyNo){
	let url="/reply/update.do?replyNo="+replyNo;
	const replyLiNode=document.getElementById("replyLi"+replyNo);
	let resp=await fetch(url);
	if(resp.status==200){
		let textHtml=await resp.text();
		replyLiNode.innerHTML=textHtml;
	}
}

async function deletReply(replyNo, boardNo){
	let url="/reply/delete.do?replyNo="+replyNo;
	let resp=await fetch(url);
	let msg="";
	if(resp.status==200){
		let checkStatus=await resp.json();
		switch(checkStatus.status){
			case -2: msg="작성자만 삭제 가능"; break;
			case -1: msg="로그인 해주세요"; break;
			case 0: msg="삭제 실패"; break;
			case 1: 
				msg="삭제 성공"; 
				readReplyList(boardNo);
				break;
		}
	}
	alert(msg);
}

async function updateReply(formNode){
	let url="/reply/update.do";
	let boardNo=formNode.boardNo.value;
	let data=new FormData(formNode);
	let resp=await fetch(url, {body:data, method:"post"});
	let msg="";
	if(resp.status==200){
		let checkStatus=await resp.json();
		switch(checkStatus.status){
			case -2: msg="작성자만 수정 가능"; break;
			case -1: msg="로그인 해주세요"; break;
			case 0: msg="수정 실패"; break;
			case 1: 
				msg="수정 성공"; 
				readReplyList(boardNo);
				break; 
		}
	}
	alert(msg);
}

// 좋아요
async function boardPreferModify(e, prefer) {
	let boardNo=e.target.value
	let url="/board/prefer.do?boardNo="+boardNo+"&prefer="+prefer+"&userNo=1";
	let msg=(prefer==1)?"좋아요":"";
	let resp=await fetch(url);
	if(resp.status==200){
		let checkStatus=resp.json();
		switch(checkStatus.status){
			case -1: msg="로그인 하세요"; break;
			case 0: msg="삭제 실패"; break;
			case 1: msg="등록 성공"; break;
			case 2: msg="삭제 성공";
		}
		if(checkStatus.status>0){
			let resp=await fetch("/board/preferDetail.do?boardNo="+boardNo+"&userNo=1");
			if(resp.status==200){
				let text=await resp.text();
				preferContainer.innerHTML=text;
			}
		}
	}
}
