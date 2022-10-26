userLoginForm.addEventListener('submit', function(event){
		event.preventDefault();
		let userId = userLoginForm.userId.value;
		let pw = userLoginForm.pw.value;
	
		var data = new Object() ;
		data.username = userId ;
		data.password = pw ;
		
		let jsonData = JSON.stringify(data) ;
		console.log(jsonData) ;
		
        fetch("http://localhost:8088/login",{
            method : "POST",
            headers :{
                "Content-Type" : "application/json; charset=utf-8"
            },
            body: JSON.stringify(data)  // json ->  qs로 변경
        }) .then((res) => {
				console.log(res.status);
				if(res.status == 500){
					alert("로그인 실패")
				}else if(res.status == 401){
					alert("비밀번호를 확인해주세요.")
				}else{
					 let jwtToken = res.headers.get("Authorization");
				    console.log(jwtToken);
	                localStorage.setItem("Authorization", jwtToken);
	                alert("로그인 성공")
				}
			});
	
})


/*
function login(){
		let userId = userLoginForm.userId.value;
		let pw = userLoginForm.pw.value;
	
		var data = new Object() ;
		data.username = userId ;
		data.password = pw ;
		
		let jsonData = JSON.stringify(data) ;
		console.log(jsonData) ;
		alert(jsonData);
		
        fetch("http://localhost:8088/login",{
            method : "POST",
            headers :{
                "Content-Type" : "application/json; charset=utf-8"
            },
            body: JSON.stringify(data)  // json ->  qs로 변경
        }).then(res=>{res.json()})
         .then(res => {
				console.log(res);
			    let jwtToken = res.headers.get("Authorization");
			    console.log(jwtToken);
                localStorage.setItem("Authorization", jwtToken);
                alert(res);
			});
}
*/