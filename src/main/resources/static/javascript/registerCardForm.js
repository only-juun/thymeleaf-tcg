function submit() {
    let cardForm = document.getElementById('cardForm')
    cardForm.submit()
}

function cancel(memberId) {
    location.href = `/members/${memberId}`
}

// Spring 컨트롤러의 엔드포인트 URL
const url = '/games';

// 셀렉트 박스에 옵션을 추가하는 함수
function populateSelectBox(gameList) {
    const selectBox = document.getElementById('game'); // 게임 셀렉트 박스 가져오기
    gameList.forEach(function(game) {
        const option = document.createElement('option'); // 새로운 옵션 요소 생성
        option.value = game; // 옵션의 값 설정
        option.text = game; // 옵션의 텍스트 설정
        selectBox.appendChild(option); // 셀렉트 박스에 옵션 추가
    });
}

// Spring 컨트롤러에 GET 요청 보내기
fetch(url)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json(); // JSON 형태로 응답 데이터 반환
    })
    .then(data => {
        // 여기서 data는 Spring 컨트롤러에서 반환한 List<String> 객체
        populateSelectBox(data); // 셀렉트 박스에 옵션 추가
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
