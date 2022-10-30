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
const replyInsertForm=document.forms["replyInsertForm"];
const replyList=document.getElementById("replyList");

if(replyInsertForm!=null){
	replyInsertForm.addEventListener("submit",insertReply);
}

async function insertReply(e){
	e.preventDefault();
	const replyInsertForm=e.target;
	let boardNo=replyInsertForm.boardNo.value;
	let url=replyInsertForm.action;
	let data=new FormData(replyInsertForm); 
	
	let resp=await fetch(url,{body:data,method:"post"});
	if(resp.status==200){//로그인이 안되어 있을때
		let json=await resp.json();
		let msg="";
		switch(json.status){
			case 0: msg="등록실패(db에러)"; break;
			case 1: 
				msg="등록성공"; 
				readReplyList(boardNo);
				break;
			case -1: msg="로그인을 하세요."; break;
		}
		alert(msg);
	}
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
	} else if(resp.status==400){
		alert("로그인 해주세여");
	} else if(resp.status==401){
		alert("글쓴이만 수정 가능");
	} else if(resp.status==500){
		alert("수정 폼 불러오기 실패")
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
const preferContainer=document.getElementById("preferContainer");

async function boardPreferModify(e, prefer) {
	let boardNo=e.target.value
	let url="/board/prefer.do?boardNo="+boardNo+"&prefer="+prefer;
	let resp=await fetch(url);
	if(resp.status==200){
		let checkStatus=await resp.json();
		if(checkStatus.status>0){
			boardPreferChange(boardNo);
		}
	}
}

async function boardPreferChange(boardNo){
	let url="/board/preferDetail.do?boardNo="+boardNo;
	let resp=await fetch(url);
	if(resp.status==200){
		let text=await resp.text();
		preferContainer.innerHTML=text;
	}
}

// 카테고리 선택
// 한개의 체크박스만 선택
function clickCheck(target) {
    document.querySelectorAll(`input[type=checkbox]`)
        .forEach(el => el.checked = false);

    target.checked = true;
}

async function selectCatergory(){
	const checks = "input[type=checkbox][name=category]:checked";
	const category=document.querySelector(checks).value;
	let url="/board/selectCatergory.do?category="+category;
	let resp=await fetch(url);
	if(resp.status==200){
		let text= await resp.text();
		let mainBoardContainer=document.getElementById("mainBoardContainer");
		mainBoardContainer.innerHTML=text;
	}
}

// 다중 선택 에러남
//async function selectCatergory(){
//	let categorys=[]
//	const checks = "input[type=checkbox][name=category]:checked";
//	const category=document.querySelectorAll(checks);
//	let url="/board/selectCatergory.do?";
//	for(let i=0; i<category.length; i++){
//		categorys.push(category[i].value);
//	}
//	let resp=await fetch(url, {body:"categorys":categorys});
//}