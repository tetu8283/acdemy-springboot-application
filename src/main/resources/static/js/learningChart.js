function drawLearningChart() {
    // JSONからデータをパース
    const months = JSON.parse(monthsJson);
    const learningTimes = [
        JSON.parse(monthBeforeLastLearningTimeJson),
        JSON.parse(lastMonthLearningTimeJson),
        JSON.parse(thisMonthLearningTimeJson),
    ];

    const categoryLabels = ["バックエンド", "フロントエンド", "インフラ"];
    const monthNames = ["先々月", "先月", "今月"];
    const categoryOrder = [0, 1, 2];

    // 各カテゴリの合計学習時間を取得
    const categoryData = categoryOrder.map(categoryType => 
        learningTimes.map(monthData => 
            monthData.reduce((total, item) => item.category_type === categoryType ? total + item.total_learning_time : total, 0)
        )
    );

    // グラフの描画
    const ctx = document.getElementById("learningChart").getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: monthNames,
            datasets: categoryLabels.map((label, index) => ({
                label: label,
                data: categoryData[index],
                backgroundColor: [
                    'rgba(255, 130, 172, 0.5)', // バックエンド
                    'rgba(235, 149, 52, 0.5)', // フロントエンド
                    'rgba(235, 213, 52, 0.5)'  // インフラ
                ][index]
            }))
        },
        options: {
            responsive: false,
            plugins: {
                title: {
                    display: true,
                    text: 'Chart.js Bar Chart',
                    font: {
                        size: 14
                    }
                },
                legend: {
                    position: 'top',
                    labels: {
                        font: {
                            size: 14
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    ticks: {
                        stepSize: 10
                    },
                    title: {
                        display: true
                    }
                },
                x: {
                    title: {
                        display: true
                    }
                }
            }
        }
    });
}

// ページが読み込まれたときにグラフを描画する関数を実行
window.onload = drawLearningChart;