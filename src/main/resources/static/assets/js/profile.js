let host = "http://localhost:8080/rest";
const app = angular.module("java6", []);
app.controller("profile", function($scope, $http){
    $scope.customer = {}
    $scope.load_all = function(){
        var url = `${host}/profile`;
        $http.get(url).then(resp => {
            $scope.customer = resp.data;
            console.log("Success", resp);
        }).catch(error => {
            console.log("Error", error)
        });
    }
    $scope.load_all();
})