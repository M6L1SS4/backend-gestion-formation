# Test final de vérification de l'API

Write-Host "=== Test de vérification final ==="

# Test d'authentification
$authBody = @{
    email = "admin@formation.com"
    motDePasse = "admin123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $authBody
    
    Write-Host "✅ Authentification réussie!"
    Write-Host "Token: $($response.token.Substring(0, 30))..."
    Write-Host "Role: $($response.role)"
    Write-Host "Email: $($response.email)"
    Write-Host "Nom: $($response.nom) $($response.prenom)"
    
    # Test de récupération des cours
    $headers = @{
        "Authorization" = "Bearer $($response.token)"
    }
    
    $cours = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/cours" -Method Get -Headers $headers
    Write-Host ""
    Write-Host "✅ Test API Admin réussi!"
    Write-Host "Nombre de cours: $($cours.Count)"
    
    if ($cours.Count -gt 0) {
        Write-Host "Premier cours: $($cours[0].titre)"
    }
    
    Write-Host ""
    Write-Host "🎉 Backend Spring Boot 100% opérationnel!"
    
} catch {
    Write-Host "❌ Erreur: $($_.Exception.Message)"
}
