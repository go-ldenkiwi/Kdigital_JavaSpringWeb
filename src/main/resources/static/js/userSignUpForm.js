userSignUpForm.userId.addEventListener('change', function(event){
	userSignUpForm.userId.classList.remove("is-invalid");
	userSignUpForm.userId.classList.remove("is-valid");
	let userId = userSignUpForm.userId.value;
	let url = "/user/checkUserId.do?userId="+userId;
	
	if(userId.length>6 && userId.length < 10)
	  	{
			userSignUpForm.userId.classList.add("is-valid");
			fetch(url)
			.then((res)=>res.json())
			.then((res)=>{
				if(res.check == 1) // 아이디가 존재할 시
				{
				userIdInvalid.innerText = "이미 아이디가 존재합니다.";
				userSignUpForm.userId.classList.add("is-invalid");
				}
				});
		}
		else if(userId.length<6){
		    userIdInvalid.innerText = "6자리 이상 입력해주세요.";
			userSignUpForm.userId.classList.add("is-invalid");
		}
		else if(userId.length>10){
			userIdInvalid.innerText = "10자리 이하로 입력해주세요.";
			userSignUpForm.userId.classList.add("is-invalid");
		}else{
			userSignUpForm.userId.classList.add("is-valid");
		}
})

userSignUpForm.pw.addEventListener('change', function(event){
	
	userSignUpForm.pw.classList.remove("is-invalid");
	userSignUpForm.pw.classList.remove("is-valid");
	let pw = userSignUpForm.pw.value;
	let num = pw.search(/[0-9]/g);
 	let eng = pw.search(/[a-z]/ig);
 	let spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
 	
 		if(pw.length < 8 || pw.length > 20){
		  passwordInvalid.innerText = "8자리 ~ 20자리 이내로 입력해주세요.";
		  userSignUpForm.pw.classList.add("is-invalid");
		 }else if(pw.search(/\s/) != -1){
		  passwordInvalid.innerText = "공백 없이 입력해주세요.";
		  userSignUpForm.pw.classList.add("is-invalid");
		 }else if( (num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0) ){
			passwordInvalid.innerText = "영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.";
			userSignUpForm.pw.classList.add("is-invalid");
		 }else{
			userSignUpForm.pw.classList.add("is-valid");
		}
})

userSignUpForm.pwck.addEventListener('change', function(event){
	userSignUpForm.pwck.classList.remove("is-invalid");
	userSignUpForm.pwck.classList.remove("is-valid");
	let pw = userSignUpForm.pw.value;
	let pwck = userSignUpForm.pwck.value;
	
		if(pwck != pw){
			userSignUpForm.pwck.classList.add("is-invalid");
		}else{
			userSignUpForm.pwck.classList.add("is-valid");
		}
})


userSignUpForm.email.addEventListener('change', function(event){
	userSignUpForm.email.classList.remove("is-invalid");
	userSignUpForm.email.classList.remove("is-valid");
    let email = userSignUpForm.email.value;
    let emailpattern = email.search(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{3,3}$/i);
    
    	if(emailpattern){
			emailInvalid.innerText = "올바른 이메일 형식을 입력해주세요.";
			userSignUpForm.email.classList.add("is-invalid");
		}else{
			userSignUpForm.email.classList.add("is-valid");
		} 
})

userSignUpForm.username.addEventListener('change', function(event){
	userSignUpForm.username.classList.remove("is-invalid");
	userSignUpForm.username.classList.remove("is-valid");
	let username= userSignUpForm.username.value;
	let usernamepattern = username.search(/^[가-힣]{2,6}$/);
	
		if(usernamepattern){
			usernameInvalid.innerText = "이름을 올바르게 입력해주세요.";
			userSignUpForm.username.classList.add("is-invalid");
		}else{
			userSignUpForm.username.classList.add("is-valid");
		}
})

userSignUpForm.nickname.addEventListener('change', function(event){
	
	userSignUpForm.nickname.classList.remove("is-invalid");
	userSignUpForm.nickname.classList.remove("is-valid");
	let nickname = userSignUpForm.nickname.value;
	let nickNamepattern = nickname.search(/^[0-9a-zA-Z]{4,10}$/i);
	
		if(nickNamepattern)
		{
			nicknameInvalid.innerText = "4-10 영어,숫자 조합을 이용해주세요. ";
			userSignUpForm.nickname.classList.add("is-invalid");
		}
		else{
			console.log(nickNamepattern);
			userSignUpForm.nickname.classList.add("is-valid");
		}
})

userSignUpForm.addEventListener('click', function(event){
	 const forms = document.querySelectorAll('.needs-validation')
	 Array.from(forms).forEach(form => {
		/*	for(let i = 0 ; i <form.length ; i ++){
		console.log(form[i].id);		
		form[i].id == userId  }*/
		    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }
    }, false)

  })
})