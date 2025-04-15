<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Orden con MIMO</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #98D8C8; /* Color primario */
        }
        h1 {
            color: #614385; /* Color Mírate */
        }
        .container {
            background-color: #F5E6A8; /* Color acento */
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #89CFF0; /* Color secundario */
            color: #333;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #27AE60; /* Color Ordena */
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenido a Orden con MIMO</h1>
        <p>${mensaje}</p>
        <a href="/tareas">Ver tareas</a>
    </div>
</body>
</html>