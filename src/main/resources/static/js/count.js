// クラス名「self-introduction」を持つ最初の要素（<textarea>タグ）を取得し、変数textareaに保存します。
const textarea = document.querySelector('.self-introduction');

// クラス名「string_num」を持つ最初の要素（<span>タグなど）を取得し、変数string_numに保存します。
// この要素は、入力された文字数を表示するためのものです。
const string_num = document.querySelector('.string_num');

// テキストエリアに対して「keyup」（キーを離した時）のイベントリスナーを追加します。
// ユーザーがキーを押して離したときに「onKeyUp」関数が呼び出されるようにします。
textarea.addEventListener('keyup', onKeyUp);

// キーボードのキーが離されたときに実行される関数を定義します。
function onKeyUp() {
    // テキストエリアに現在入力されているテキストの内容を取得し、変数inputTextに保存します。
    const inputText = textarea.value;

    // 取得したテキストの長さ（文字数）を計算し、その数を「string_num」要素の中に表示します。
    // 例えば、入力されたテキストが「Hello」なら、その長さは5であり、「string_num」に5が表示されます。
    string_num.innerText = inputText.length;
}
