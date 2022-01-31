def
	N = new(676)
in
	while (*N != 1) do
		if (2*(*N/2) == *N) then 
			N := *N/2
		else
			N := 3 * *N + 1
		end ;
		println *N
	end ;
	println 2
end ;;