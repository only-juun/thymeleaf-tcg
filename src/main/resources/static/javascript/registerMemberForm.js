function submit() {
    let memberForm = document.getElementById('memberForm')
    memberForm.submit()
}

function cancel() {
    location.href = '/members/list'
}