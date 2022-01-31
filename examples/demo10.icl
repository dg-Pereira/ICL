def
	N = new(100)
in
	while (*N != 1) do
		if (*N % 2 == 0) then 
			N := *N/2
		else
			N := 3 * *N + 1
		end ;
		println *N
	end ;
	println 2
end ;;