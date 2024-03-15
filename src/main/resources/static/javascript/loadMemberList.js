function moveToRegisterForm() {
    location.href = '/members/registerForm';
}

function search() {
    let name = document.getElementById('name').value;
    let level = document.getElementById('level').value;

    axios.get('/members/search', {
        params: {
            name: name,
            level: level
        }
    })
        .then(function(response) {
            let fragment = response.data;
            console.log(fragment); // 확인을 위해 응답을 콘솔에 출력합니다.
            document.getElementById('table-container').innerHTML = fragment;
        })
        .catch(function(error) {
            if (error.response) {
                alert('회원 리스트 검색이 실패했습니다. \n원인 => ' + error.response.data);
                console.error(error);
                console.error('exception = ' + error.response.data);
            } else {
                alert('서버 응답이 없습니다.');
                console.error(error);
            }
        });
}
