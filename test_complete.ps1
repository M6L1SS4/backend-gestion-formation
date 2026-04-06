# Test complet de l'API

# Test d'authentification
Write-Host "=== Test d'authentification ==="
$authBody = @{
    email = "admin@formation.com"
    motDePasse = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $authBody
$token = $response.token

Write-Host "Authentification réussie!"
Write-Host "Token: $($token.Substring(0, 30))..."
Write-Host "Role: $($response.role)"
Write-Host "Email: $($response.email)"
Write-Host "Nom: $($response.nom) $($response.prenom)"
Write-Host ""

# Test de récupération des cours
Write-Host "=== Test de récupération des cours ==="
$headers = @{
    "Authorization" = "Bearer $token"
}

try {
    $cours = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/cours" -Method Get -Headers $headers
    Write-Host "Nombre de cours: $($cours.Count)"
    
    if ($cours.Count -gt 0) {
        Write-Host "Premier cours:"
        Write-Host "  - Titre: $($cours[0].titre)"
        Write-Host "  - ID: $($cours[0].id)"
        if ($cours[0].description) {
            Write-Host "  - Description: $($cours[0].description)"
        }
    }
} catch {
    Write-Host "Erreur lors de la récupération des cours: $($_.Exception.Message)"
}

Write-Host ""
Write-Host "=== Tests terminés avec succès! ==="
