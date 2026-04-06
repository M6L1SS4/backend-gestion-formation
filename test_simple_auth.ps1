# Test simple d'authentification

Write-Host "Test de connexion à l'API..."

try {
    # Test si l'API est accessible
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method Get
    Write-Host "API accessible (GET)"
} catch {
    Write-Host "API non accessible en GET - Normal, il faut utiliser POST"
}

# Test POST avec authentification
Write-Host "Test POST authentification..."
$authBody = @{
    email = "admin@formation.com"
    motDePasse = "admin123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $authBody
    Write-Host "Authentification réussie!"
    Write-Host "Token: $($response.token.Substring(0, 30))..."
    Write-Host "Role: $($response.role)"
} catch {
    Write-Host "Erreur d'authentification: $($_.Exception.Message)"
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
}
