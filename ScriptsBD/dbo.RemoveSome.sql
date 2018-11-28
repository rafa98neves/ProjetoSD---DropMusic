/****** Object:  StoredProcedure [dbo].[RemoveSome]    Script Date: 21/11/2018 01:35:10 ******/
DROP PROCEDURE [dbo].[RemoveSome]
GO

/****** Object:  StoredProcedure [dbo].[RemoveSome]    Script Date: 21/11/2018 01:35:10 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:      <Author, , Name>
-- Create Date: <Create Date, , >
-- Description: <Description, , >
-- =============================================
CREATE PROCEDURE [dbo].[RemoveSome]
(
    @UserID int,
	@Tipo NVARCHAR(50),
    @Nome NVARCHAR(50),
	@Adiciona NVARCHAR(50),
	@AdicionaNome NVARCHAR(100),
	@Erro int OUTPUT,
	@resultMessage VARCHAR(1000) OUTPUT
)
AS
BEGIN
    SET @Erro = -1;
	SET @resultMessage = '';

	DECLARE @MusicaID INT;
	
	IF @Tipo = 'musica'
	BEGIN
		SET @MusicaID = -1;

		SELECT @MusicaID = MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE MusInf.Titulo =  @Nome


		DECLARE @ArtistaID INT;
		SET @ArtistaID = -1;

		SELECT @ArtistaID = Art.ArtistID
		FROM dbo.Artist Art
		WHERE @AdicionaNome = Art.Name

		DELETE FROM dbo.Music
		WHERE MusicInfo = @MusicaID AND Artist = @ArtistaID
	END

	IF @Tipo = 'album'
	BEGIN
		DECLARE @AlbumID INT;
		SET @AlbumID = -1;

		SELECT @AlbumID = AlbInf.AlbumInfoID
		FROM dbo.AlbumInfo AlbInf
		WHERE @Nome = AlbInf.Name

		SET @MusicaID = -1;

		SELECT @MusicaID = MusInf.MusicInfoID
		FROM dbo.MusicInfo MusInf
		WHERE @AdicionaNome = MusInf.Titulo

		DELETE FROM dbo.Album
		WHERE AlbumInfo = @AlbumID AND Music = @MusicaID
	END

	SET @resultMessage = ' ; notificate | none';
	
	SELECT @resultMessage = ' ; notificate | '+ UserDB.username + @resultMessage
	FROM dbo.Historicoo Hist, dbo.Users UserDB 
	WHERE Hist.Tipo = @Tipo AND Hist.Nome = @Nome AND Hist.UsernameID = UserDB.UserID AND Hist.UsernameID != @UserID

	INSERT INTO dbo.Historicoo
	(
		Tipo,
		Nome,
		UsernameID
	)
	VALUES
	(
		@Tipo,
		@Nome,
		@UserID
	)

	SET @Erro = 1;
	RETURN;
END
GO


