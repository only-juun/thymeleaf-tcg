function submit() {
    let memberForm = document.getElementById('memberForm')
    memberForm.submit()
}

function cancel(memberId) {
    location.href = `/members/${memberId}`
}