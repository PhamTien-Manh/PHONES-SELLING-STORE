let host = "/rest";
const app = angular.module("java6", []);
app.controller("home", function($scope, $http){
    $scope.items = []
    $scope.load_all = function(){
        var url = `${host}/home`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
            console.log("Success", resp);
        }).catch(error => {
            console.log("Error", error)
        });
    };
    $scope.loadById = function(category){
        var url = `${host}/home/category/${category}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
            console.log("Success", resp);
        }).catch(error => {
            console.log("Error", error)
        });
    };
    $scope.goToDetail = function(idDetail) {
        var url = `${host}/home/${idDetail}`;
        $http.get(url).then(resp => {
            $scope.productDetail = resp.data;
            console.log("Success", resp);
            scrollToTop(); // Gọi hàm scrollToTop để đẩy trang lên đầu
        }).catch(error => {
            console.log("Error", error)
        });
    }
    // Lấy parameter từ URL
    const urlParams = new URLSearchParams(window.location.search);
    let category = urlParams.get('category')
    console.log(category);

    if (category !== null) {
        $scope.loadById(category);
    } else {
        $scope.load_all();
    }
    function scrollToTop() {
        window.scrollTo({
            top: 0,
            behavior: "smooth" // Sử dụng "smooth" để tạo hiệu ứng cuộn mượt
        });
    }
})