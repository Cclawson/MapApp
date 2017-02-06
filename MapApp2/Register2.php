<?php

    $connect = mysqli_connect("mobileapptest.000webhostapp.com", "id728936_cclawson", "password", "id728936_footnote");

    $name = $_POST["name"];
    $username = $_POST["username"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "INSERT INTO user (name, username, password) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sss", $name, $username, $password);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
    
    echo json_encode($response);
?>
