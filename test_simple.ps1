# Test simple de l'API

# Test d'authentification
$body = @{
    email = "admin@formation.com"
    motDePasse = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $body
$token = $response.token

Write-Host "Token: $($token.Substring(0, 30))..."

# Test de récupération des cours
$headers = @{
    "Authorization" = "Bearer $token"
}

$cours = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/cours" -Method Get -Headers $headers
Write-Host "Nombre de cours: $($cours.Count)"
