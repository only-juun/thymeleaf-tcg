
function modifyMember(memberId) {
    location.href = '/members/modifyForm?memberId=' + memberId
}

function deleteMember(memberId) {
    if (confirm('회원을 삭제하시겠습니까?')) {
        axios({
            method : 'DELETE',
            url: '/members/' + memberId
        }).then(() => {
            alert("회원 삭제가 성공했습니다.")
            location.href = '/members/list'
        }).catch(ex => {
            alert('회원 삭제가 실패했습니다. \n원인 => ' + ex.response.data)
            console.error(ex);
            console.error('exception = ' + ex.response.data)
        });
    }
}

function deleteCard(cardId, memberId) {
    if (confirm('카드를 삭제하시겠습니까?')) {
        axios({
            method : 'DELETE',
            url: '/cards/' + cardId + '?memberId=' + memberId
        }).then(() => {
            alert("카드 삭제가 성공했습니다.")
            location.reload();
        }).catch(ex => {
            alert('카드 삭제가 실패했습니다. \n원인 => ' + ex.response.data)
            console.error(ex);
            console.error('exception = ' + ex.response.data)
        });
    }
}

function back() {
    location.href = '/members/list'
}

function addCard(memberId) {
    location.href = '/cards/registerForm?memberId=' + memberId;
}
