
document.addEventListener("DOMContentLoaded", function() {

    // 削除モーダル表示フラグのチェック
    // htmlのid=deleteModalFlagを探してflagを取得
    var deleteModalFlag = document.getElementById("deleteModalFlag");
    if (deleteModalFlag) {
        // idがdeleteModalMsgのものを探してmodalに格納
        var modal = document.getElementById("deleteModalMsg");
        if (modal) {
          // モーダルにshowを追加する(showはcssでdisplay: none;をdisplay: block;を適応してモーダルを表示する)
            modal.classList.add("show");
            modal.style.display = "block";
        }
    }
  
    // 更新モーダル表示フラグのチェック
    var updateModalFlag = document.getElementById("updateModalFlag");
    if (updateModalFlag) {
        // 更新モーダルを表示
        var updateModal = document.getElementById("updateModalMsg");
        if (updateModal) {
            updateModal.classList.add("show");
            updateModal.style.display = "block";
        }
    }
    
    // 登録モーダル表示フラグのチェック
    var createModalFlag = document.getElementById("createModalFlag");
    if (createModalFlag) {
        // 登録モーダルを表示
        var createModal = document.getElementById("createModalMsg");
        if (createModal) {
            createModal.classList.add("show");
            createModal.style.display = "block";
        }
    }
  });