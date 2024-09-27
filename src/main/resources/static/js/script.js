// 要素を取得
var modal = document.getElementById("delete-modal");
var btn = document.getElementById("delete-modal-btn");
var span = document.getElementsByClassName("close")[0];

// ボタンをクリックするとモーダルを開く
btn.onclick = function() {
    modal.style.display = "block";
}

// モーダルの外側をクリックすると閉じる
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
