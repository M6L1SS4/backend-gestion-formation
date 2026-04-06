# Test final de l'API

# Test d'authentification
Write-Host "Test d'authentification..."
$authBody = @{
    email = "admin@formation.com"
    motDePasse = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $authBody
$token = $response.token

Write-Host "Token obtenu: $($token.Substring(0, 30))..."

# Test de récupération des cours
Write-Host "Test de récupération des cours..."
$headers = @{
    "Authorization" = "Bearer $token"
}

$cours = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/cours" -Method Get -Headers $headers
Write-Host "Nombre de cours: $($cours.Count)"

if ($cours.Count -gt 0) {
    Write-Host "Premier cours: $($cours[0].titre)"
}

Write-Host "Tests terminés avec succès!"
